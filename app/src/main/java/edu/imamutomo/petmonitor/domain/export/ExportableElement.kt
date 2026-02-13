package edu.imamutomo.petmonitor.domain.export

interface ExportableElement {
    fun accept(visitor: DataExportVisitor): String
}