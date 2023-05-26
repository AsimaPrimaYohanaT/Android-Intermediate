package com.example.mystoryapp.ui.mainmenu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.mystoryapp.*
import com.example.mystoryapp.adapter.ListStoryAdapter
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.UserPreference
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainMenuViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var preference: UserPreference
    private lateinit var loginViewModel: LoginViewModel
    val token = "token"

    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        if (::preference.isInitialized) {
            val dummyStory = DataDummy.generateDummyStoryResponse()
            val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStory)
            val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
            expectedStory.value = data
            Mockito.`when`(storyRepository.getStory(token)).thenReturn(expectedStory)

            loginViewModel.saveUser(UserModelDummy.generateDummyUserModel())
            val mainmenuViewModel = MainMenuViewModel(preference,storyRepository)
            val actualStory: PagingData<ListStoryItem> = mainmenuViewModel.story(token).getOrAwaitValue()

            val differ = AsyncPagingDataDiffer(
                diffCallback = ListStoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(actualStory)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(dummyStory.size, differ.snapshot().size)
            Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
        }
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        if (::preference.isInitialized) {
            val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
            val expectedQuote = MutableLiveData<PagingData<ListStoryItem>>()
            expectedQuote.value = data
            Mockito.`when`(storyRepository.getStory(token)).thenReturn(expectedQuote)
            val mainmenuViewModel = MainMenuViewModel(preference,storyRepository)
            val actualQuote: PagingData<ListStoryItem> = mainmenuViewModel.story(token).getOrAwaitValue()
            val differ = AsyncPagingDataDiffer(
                diffCallback = ListStoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(actualQuote)
            Assert.assertEquals(0, differ.snapshot().size)
        }
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}