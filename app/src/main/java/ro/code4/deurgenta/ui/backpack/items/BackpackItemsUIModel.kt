package ro.code4.deurgenta.ui.backpack.items

import ro.code4.deurgenta.data.model.BackpackItem

sealed class BackpackItemsUIModel

data class Success(val items: List<BackpackItem>) : BackpackItemsUIModel()

data class Error(val throwable: Throwable) : BackpackItemsUIModel()

object Loading : BackpackItemsUIModel()