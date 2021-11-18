package fr.stardustenterprises.deface.patcher.api

import org.objectweb.asm.tree.MethodNode

@FunctionalInterface
interface ILocation {
    fun insertPatch(methodNode: MethodNode, patchData: IPatch)
}