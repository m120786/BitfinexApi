package com.vb.bitfinexapi.viewmodel

import com.google.common.truth.Truth.assertThat
import com.vb.bitfinexapi.model.domainModel.BookModel
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.internal.toImmutableList
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

class TickerViewModelTest {

    private lateinit var viewModel: TickerViewModel

    private val requestObjBook = JSONObject()
    private var list = listOf<BookModel?>()
    private var list2 = emptyList<BookModel?>()

    @Before
    fun setup() {
        viewModel = TickerViewModel(CoinFakeRepository())
    }

    @Test
    fun `collect book data for list`() {
        runBlockingTest {
            list = viewModel.coinService.subscribeToBook(requestObjBook).take(10).toList()
            if (list.size > 9) {
                list2 = list.toImmutableList().reversed()
            }
        }
        assertThat(list.reversed()).isEqualTo(list2)
    }
}