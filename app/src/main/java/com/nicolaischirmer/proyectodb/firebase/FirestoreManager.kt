package com.nicolaischirmer.proyectodb.firebase

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.nicolaischirmer.proyectodb.firebase.model.Weapons
import com.nicolaischirmer.proyectodb.firebase.model.WeaponsDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager(auth: AuthManager, context: Context){
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.getCurrentUser()?.uid

    companion object {
        const val WEAPONS_COLLECTION = "weapons"
    }

    fun getWeapons(): Flow<List<Weapons>> {
        return firestore.collection(WEAPONS_COLLECTION)
            .whereEqualTo("userId", userId)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    ds.toObject(WeaponsDB::class.java)?.let { weaponsDB ->
                        Weapons(
                            id = ds.id,
                            userId = weaponsDB.userId,
                            name = weaponsDB.name,
                            type = weaponsDB.type,
                            description = weaponsDB.description,
                            damage = weaponsDB.damage
                        )
                    }
                }
            }
    }

    suspend fun addWeapons(weapons: Weapons) {
        firestore.collection(WEAPONS_COLLECTION).add(weapons).await()
    }

    suspend fun updateWeapons(weapons: Weapons) {
        val weaponsRef = weapons.id?.let {
            firestore.collection(WEAPONS_COLLECTION).document(it)
        }
        weaponsRef?.set(weapons)?.await()
    }

    suspend fun deleteWeaponById(weaponId: String) {
        firestore.collection("weapons").document(weaponId).delete().await()
    }

    suspend fun getWeaponById(id: String): Weapons? {
        return firestore.collection(WEAPONS_COLLECTION).document(id)
            .get().await().toObject(WeaponsDB::class.java)?.let {
                Weapons(
                    id = id,
                    userId = it.userId,
                    name = it.name,
                    type = it.type,
                    description = it.description,
                    damage = it.damage
                )
            }!!
    }


}