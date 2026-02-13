package edu.imamutomo.petmonitor.domain.export

import java.io.File

data class ExportResult(val file: File,
                        val filename: String,
                        val mimeType: String,
                        val recordCount: Int)

enum class ExportFormat { JSON, XML, CSV }