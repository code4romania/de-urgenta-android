package ro.code4.deurgenta.repositories

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ro.code4.deurgenta.data.dao.BackpackDao
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemType
import ro.code4.deurgenta.data.model.requests.CreateNewBackpackRequests
import ro.code4.deurgenta.data.model.requests.asNewBodyRequest
import ro.code4.deurgenta.data.model.requests.asUpdateBodyRequest
import ro.code4.deurgenta.data.model.requests.toModel
import ro.code4.deurgenta.data.model.response.toModel
import ro.code4.deurgenta.services.BackpackService

class BackpacksRepository(
    private val backpacksDao: BackpackDao,
    private val backpacksService: BackpackService
) {

    fun getBackpacks(): Single<List<Backpack>> {
        val observableDb = backpacksDao.getAllBackpacks()
        val observableApi = backpacksService.getBackpacks().map { responseBackpacks ->
            responseBackpacks.map { it.toModel() }
        }
        return Single.zip(
            observableDb,
            observableApi.onErrorReturnItem(emptyList())
        ) { backpacksDb, backpacksApi ->
            val areAllBackpacksInDb = backpacksDb.containsAll(backpacksApi)
            if (!areAllBackpacksInDb) {
                backpacksDao.saveBackpack(backpacksApi)
            }
            // No data loaded from back end
            if (backpacksApi.isEmpty()) {
                // Return cached backpacks
                backpacksDb
            } else {
                // Return the fresher data
                backpacksApi
            }
        }
    }

    fun saveNewBackpack(name: String): Single<Backpack> {
        return backpacksService.saveNewBackpack(CreateNewBackpackRequests(name)).map { response ->
            val backpack = response.toModel()
            backpacksDao.saveBackpack(listOf(backpack))
            return@map backpack
        }
    }

    fun getAvailableItemsForCategory(backpack: Backpack, type: BackpackItemType): Observable<List<BackpackItem>> {
        return backpacksDao.getAvailableItemsForCategory(backpack.id, type)
            .startWith(
                backpacksService.getAvailableItemsForCategory(backpack.id, type.id)
                    .map { response ->
                        updateLocalItemsIfNeeded(backpack, type, response.map { it.toModel() })
                        emptyList<BackpackItem>()
                    }
                    .onErrorReturn { emptyList() }
            )
            .skip(1)
    }

    private fun updateLocalItemsIfNeeded(backpack: Backpack, type: BackpackItemType, apiItems: List<BackpackItem>) {
        val localItems = backpacksDao.getAvailableItemsForCategorySync(backpack.id, type)
        val commonItems = apiItems.intersect(localItems)
        val extraLocalItems = localItems - commonItems
        val extraApiItems = apiItems - commonItems
        backpacksDao.updateContainedItems(toBeInserted = extraApiItems, toBeDeleted = extraLocalItems)
    }

    fun saveNewBackpackItem(backpackItem: BackpackItem): Completable {
        return backpacksService.addNewItemToContent(backpackItem.backpackId, backpackItem.asNewBodyRequest())
            .flatMapCompletable { response ->
                Completable.fromAction { backpacksDao.saveBackpackItem(response.toModel()) }
            }
    }

    fun updateBackpackItem(backpackItem: BackpackItem): Completable {
        return backpacksService.updateContainedItem(backpackItem.id, backpackItem.asUpdateBodyRequest())
            .flatMapCompletable { response ->
                backpacksDao.updateBackpackItem(response.toModel())
                Completable.complete()
            }
    }

    fun deleteBackpackItem(itemId: String): Completable {
        return backpacksService.removeItemFromContent(itemId)
            .andThen(Completable.fromAction { backpacksDao.deleteContainedItem(itemId) })
    }
}
