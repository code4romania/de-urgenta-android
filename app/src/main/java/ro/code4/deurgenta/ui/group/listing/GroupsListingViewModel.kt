package ro.code4.deurgenta.ui.group.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.Disposable
import ro.code4.deurgenta.data.model.Group
import ro.code4.deurgenta.helper.SchedulersProvider
import ro.code4.deurgenta.repositories.GroupRepository
import ro.code4.deurgenta.ui.base.BaseViewModel

class GroupsListingViewModel(
    private val groupRepository: GroupRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    private val _groupsDataModel = MutableLiveData<GroupListingState>()
    val groupsDataModel: LiveData<GroupListingState> = _groupsDataModel
    private val groupsData = mutableMapOf<Group, List<GroupListingModel>>()
    private val groupRequests = mutableMapOf<Group, Disposable?>()

    init {
        _groupsDataModel.value = GroupListingState.Loading
        groupRepository.fetchAndRefreshGroups()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.main())
            .subscribe { receivedGroups ->
                receivedGroups.forEach { groupsData[it] = listOf(GroupInfo(it)) }
                _groupsDataModel.value = GroupListingState.Success(groupsData.asUiModel())
            }
    }

    fun showMembersFor(group: Group) {
        var currentGroupContent = groupsData.getValue(group)
        groupsData[group] = listOf(currentGroupContent[0], GroupLoadingMembers)
        _groupsDataModel.value = GroupListingState.Success(groupsData.asUiModel())
        groupRequests[group] = groupRepository.fetchAndRefreshGroupMembers(group.id)
            .subscribeOn(schedulersProvider.io())
            .map { foundMembers -> foundMembers.sortedBy { it.isGroupAdmin } } // the admin is always first in the group
            .observeOn(schedulersProvider.main())
            .subscribe(
                { receivedMembers ->
                    currentGroupContent = groupsData.getValue(group)
                    val newGroupContent = mutableListOf<GroupListingModel>().apply {
                        add(currentGroupContent[0])
                        addAll(receivedMembers.map { GroupIndividual(group, it) })
                    }
                    groupsData[group] = newGroupContent
                    _groupsDataModel.value = GroupListingState.Success(groupsData.asUiModel())
                },
                {
                    _groupsDataModel.value = GroupListingState.Error(it)
                }
            )
    }

    fun hideMembersFor(group: Group) {
        val pendingRequest = groupRequests[group]
        pendingRequest?.dispose()
        groupRequests[group] = null
        val groupContent = groupsData.getValue(group)
        groupsData[group] = listOf(groupContent[0])
        _groupsDataModel.value = GroupListingState.Success(groupsData.asUiModel())
    }

    override fun onCleared() {
        super.onCleared()
        groupRequests.forEach { pendingRequest -> pendingRequest.value?.dispose() }
    }
}

private fun Map<Group, List<GroupListingModel>>.asUiModel(): List<GroupListingModel> = map { entries -> entries.value }
    .fold(mutableListOf()) { acc, modelList ->
        acc.apply { addAll(modelList) }
    }

sealed class GroupListingState {
    object Loading : GroupListingState()
    data class Success(val data: List<GroupListingModel>) : GroupListingState()
    data class Error(val exception: Throwable) : GroupListingState()
}
