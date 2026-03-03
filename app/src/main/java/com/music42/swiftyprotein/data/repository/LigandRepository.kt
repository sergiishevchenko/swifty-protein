package com.music42.swiftyprotein.data.repository

import android.content.Context
import com.music42.swiftyprotein.R
import com.music42.swiftyprotein.data.model.Ligand
import com.music42.swiftyprotein.data.parser.CifParser
import com.music42.swiftyprotein.data.remote.RcsbApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LigandRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val rcsbApi: RcsbApi
) {

    private var ligandIds: List<String>? = null

    suspend fun getLigandIds(): List<String> = withContext(Dispatchers.IO) {
        ligandIds ?: run {
            val ids = context.resources.openRawResource(R.raw.ligands)
                .bufferedReader()
                .readLines()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
            ligandIds = ids
            ids
        }
    }

    suspend fun fetchLigand(ligandId: String): Result<Ligand> = withContext(Dispatchers.IO) {
        try {
            val response = rcsbApi.getLigandCif(ligandId)
            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Server returned ${response.code()}: ${response.message()}")
                )
            }
            val body = response.body()?.string()
                ?: return@withContext Result.failure(Exception("Empty response body"))

            val ligand = CifParser.parse(ligandId, body)
            if (ligand.atoms.isEmpty()) {
                return@withContext Result.failure(Exception("No atoms found in ligand data"))
            }
            Result.success(ligand)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
