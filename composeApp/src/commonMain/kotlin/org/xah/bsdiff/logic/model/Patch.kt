package org.xah.bsdiff.logic.model

import kotlinx.serialization.Serializable

@Serializable
data class Patch(
    val source : PatchMeta,
    val target : PatchMeta
)



