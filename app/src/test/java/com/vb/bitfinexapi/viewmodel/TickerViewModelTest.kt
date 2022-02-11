package com.vb.bitfinexapi.viewmodel

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.vb.bitfinexapi.model.domainModel.BookModel
import com.vb.bitfinexapi.repository.CoinService
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.internal.toImmutableList
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

class TickerViewModelTest {

    private lateinit var viewModel: TickerViewModel

    val requestObjBook = JSONObject()
    var list = listOf<BookModel?>()
    var list2 = emptyList<BookModel?>()

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