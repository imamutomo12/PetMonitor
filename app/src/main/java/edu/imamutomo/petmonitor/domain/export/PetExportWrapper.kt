package edu.imamutomo.petmonitor.domain.export

import edu.imamutomo.petmonitor.domain.model.pet.Pet

data class PetExportWrapper(val pet: Pet) : ExportableElement {
    override fun accept(visitor: DataExportVisitor): String = visitor.visit(pet)
}
