package com.valdays.journal.util

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.valdays.journal.data.local.entity.MediaEntity
import com.valdays.journal.data.local.entity.NoteEntity
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

data class NoteBackupDTO(
    val note: NoteEntity,
    val media: List<MediaEntity>
)

object BackupRestoreUtil {

    private val gson = Gson()

    /**
     * Exports a note and its associated media to a .vldys (zip) archive.
     */
    fun exportToVldys(context: Context, note: NoteEntity, media: List<MediaEntity>, outputStream: OutputStream) {
        ZipOutputStream(BufferedOutputStream(outputStream)).use { zos ->
            // Update media entities to point to relative zip names in DTO
            val zipMediaList = media.mapIndexed { index, m ->
                m.copy(uriString = "media_$index.file")
            }

            val dto = NoteBackupDTO(note, zipMediaList)
            val noteJsonString = gson.toJson(dto)

            // 1. Write Note metadata as note.json
            val noteEntry = ZipEntry("note.json")
            zos.putNextEntry(noteEntry)
            zos.write(noteJsonString.toByteArray())
            zos.closeEntry()

            // 2. Write associated media
            media.forEachIndexed { index, mediaEntity ->
                try {
                    val uri = Uri.parse(mediaEntity.uriString)
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        // Generate a safe filename for the media in the zip
                        val fileName = "media_$index.file"
                        val mediaEntry = ZipEntry(fileName)
                        zos.putNextEntry(mediaEntry)

                        val buffer = ByteArray(1024)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } > 0) {
                            zos.write(buffer, 0, length)
                        }
                        zos.closeEntry()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Continue with other media if one fails
                }
            }
        }
    }

    /**
     * Imports a .vldys archive, extracting note.json and media files.
     * Extracts media to the app's internal storage and returns the NoteEntity and updated MediaEntity list.
     */
    fun importFromVldys(context: Context, inputStream: InputStream): Pair<NoteEntity, List<MediaEntity>> {
        var noteJsonString = ""
        val internalMediaPaths = mutableMapOf<String, String>()
        val mediaDir = File(context.filesDir, "imported_media").apply { mkdirs() }

        ZipInputStream(BufferedInputStream(inputStream)).use { zis ->
            var zipEntry = zis.nextEntry
            while (zipEntry != null) {
                if (zipEntry.name == "note.json") {
                    val reader = BufferedReader(InputStreamReader(zis))
                    noteJsonString = reader.readText()
                } else if (zipEntry.name.startsWith("media_")) {
                    // Extract media to internal storage
                    val outFile = File(mediaDir, "imported_${System.currentTimeMillis()}_${zipEntry.name}")
                    FileOutputStream(outFile).use { fos ->
                        val buffer = ByteArray(1024)
                        var len: Int
                        while (zis.read(buffer).also { len = it } > 0) {
                            fos.write(buffer, 0, len)
                        }
                    }
                    internalMediaPaths[zipEntry.name] = outFile.absolutePath
                }
                zipEntry = zis.nextEntry
            }
        }

        val dto = gson.fromJson(noteJsonString, NoteBackupDTO::class.java)

        val updatedMedia = dto.media.map { mediaEntity ->
            val internalPath = internalMediaPaths[mediaEntity.uriString] ?: mediaEntity.uriString
            mediaEntity.copy(uriString = internalPath)
        }

        return Pair(dto.note, updatedMedia)
    }
}