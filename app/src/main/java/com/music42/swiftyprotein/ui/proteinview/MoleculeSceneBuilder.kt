package com.music42.swiftyprotein.ui.proteinview

import com.google.android.filament.Engine
import com.music42.swiftyprotein.data.model.Atom
import com.music42.swiftyprotein.data.model.Ligand
import com.music42.swiftyprotein.util.CpkColors
import dev.romainguy.kotlin.math.Float3
import dev.romainguy.kotlin.math.Quaternion
import dev.romainguy.kotlin.math.normalize
import dev.romainguy.kotlin.math.cross
import dev.romainguy.kotlin.math.dot
import io.github.sceneview.geometries.Cylinder
import io.github.sceneview.geometries.Sphere
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.math.Position
import io.github.sceneview.node.MeshNode
import io.github.sceneview.node.Node
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


object MoleculeSceneBuilder {

    private const val BALL_RADIUS = 0.35f
    private const val STICK_RADIUS = 0.1f
    private const val SPHERE_STACKS = 16
    private const val SPHERE_SLICES = 16

    fun build(
        engine: Engine,
        materialLoader: MaterialLoader,
        ligand: Ligand,
        mode: VisualizationMode
    ): Pair<Node, Map<MeshNode, Atom>> {
        val parentNode = Node(engine)
        val atomNodeMap = mutableMapOf<MeshNode, Atom>()
        val atomById = ligand.atoms.associateBy { it.id }

        val centerX = ligand.atoms.map { it.x }.average().toFloat()
        val centerY = ligand.atoms.map { it.y }.average().toFloat()
        val centerZ = ligand.atoms.map { it.z }.average().toFloat()

        if (mode != VisualizationMode.STICKS_ONLY) {
            val radius = if (mode == VisualizationMode.SPACE_FILL)
                BALL_RADIUS * 2.5f else BALL_RADIUS

            for (atom in ligand.atoms) {
                val color = CpkColors.getColor(atom.element)
                val sphere = Sphere.Builder()
                    .radius(radius)
                    .center(Position(0f, 0f, 0f))
                    .stacks(SPHERE_STACKS)
                    .slices(SPHERE_SLICES)
                    .build(engine)

                val material = materialLoader.createColorInstance(
                    color = color,
                    metallic = 0.0f,
                    roughness = 0.6f,
                    reflectance = 0.3f
                )

                val meshNode = MeshNode(
                    engine = engine,
                    primitiveType = sphere.primitiveType,
                    vertexBuffer = sphere.vertexBuffer,
                    indexBuffer = sphere.indexBuffer,
                    boundingBox = sphere.boundingBox,
                    materialInstance = material
                ).apply {
                    position = Position(
                        atom.x - centerX,
                        atom.y - centerY,
                        atom.z - centerZ
                    )
                    isTouchable = true
                }

                parentNode.addChildNode(meshNode)
                atomNodeMap[meshNode] = atom
            }
        }

        if (mode != VisualizationMode.SPACE_FILL) {
            val bondColor = androidx.compose.ui.graphics.Color(0xFF888888)
            for (bond in ligand.bonds) {
                val atom1 = atomById[bond.atomId1] ?: continue
                val atom2 = atomById[bond.atomId2] ?: continue

                val pos1 = Float3(
                    atom1.x - centerX,
                    atom1.y - centerY,
                    atom1.z - centerZ
                )
                val pos2 = Float3(
                    atom2.x - centerX,
                    atom2.y - centerY,
                    atom2.z - centerZ
                )

                val mid = Float3(
                    (pos1.x + pos2.x) / 2f,
                    (pos1.y + pos2.y) / 2f,
                    (pos1.z + pos2.z) / 2f
                )

                val diff = Float3(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z)
                val length = sqrt(diff.x * diff.x + diff.y * diff.y + diff.z * diff.z)

                if (length < 0.001f) continue

                val cylinder = Cylinder.Builder()
                    .radius(STICK_RADIUS)
                    .height(length)
                    .center(Position(0f, 0f, 0f))
                    .build(engine)

                val material = materialLoader.createColorInstance(
                    color = bondColor,
                    metallic = 0.0f,
                    roughness = 0.7f,
                    reflectance = 0.2f
                )

                val meshNode = MeshNode(
                    engine = engine,
                    primitiveType = cylinder.primitiveType,
                    vertexBuffer = cylinder.vertexBuffer,
                    indexBuffer = cylinder.indexBuffer,
                    boundingBox = cylinder.boundingBox,
                    materialInstance = material
                ).apply {
                    position = Position(mid.x, mid.y, mid.z)
                    quaternion = cylinderQuaternion(diff)
                }

                parentNode.addChildNode(meshNode)
            }
        }

        return parentNode to atomNodeMap
    }

    private fun cylinderQuaternion(direction: Float3): Quaternion {
        val dir = normalize(direction)
        val up = Float3(0f, 1f, 0f)

        val d = dot(up, dir)
        if (d > 0.9999f) return Quaternion()
        if (d < -0.9999f) return Quaternion(0f, 0f, 1f, 0f)

        val axis = normalize(cross(up, dir))
        val halfAngle = acos(d.coerceIn(-1f, 1f)) / 2f
        val s = sin(halfAngle)

        return Quaternion(axis.x * s, axis.y * s, axis.z * s, cos(halfAngle))
    }
}
