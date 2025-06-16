package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tumbuhnyata.data.model.CsrItem
import com.example.tumbuhnyata.data.model.SubStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class RiwayatViewModel(dummyList: List<CsrItem>) : ViewModel() {
    private val _perluTindakanItems = MutableStateFlow(dummyList.filter {
        it.subStatus == SubStatus.MENUNGGU_PEMBAYARAN ||
                it.subStatus == SubStatus.MEMERLUKAN_REVISI ||
                it.subStatus == SubStatus.PROSES_REVIEW
    })
    val perluTindakanItems: StateFlow<List<CsrItem>> = _perluTindakanItems

    private val _diterimaItems = MutableStateFlow(dummyList.filter {
        it.subStatus == SubStatus.MENDATANG ||
                it.subStatus == SubStatus.PROGRESS ||
                it.subStatus == SubStatus.SELESAI
    })
    val diterimaItems: StateFlow<List<CsrItem>> = _diterimaItems
}package com.example.tumbuhnyata.ui.riwayat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.model.CsrItem
import com.example.tumbuhnyata.data.model.SubStatus
import com.example.tumbuhnyata.data.model.dummyCsrList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RiwayatViewModel(dummyList: List<CsrItem> = dummyCsrList) : ViewModel() {
    private val _perluTindakanItems = MutableStateFlow(dummyList.filter {
        it.subStatus == SubStatus.MENUNGGU_PEMBAYARAN ||
                it.subStatus == SubStatus.MEMERLUKAN_REVISI ||
                it.subStatus == SubStatus.PROSES_REVIEW
    })
    val perluTindakanItems: StateFlow<List<CsrItem>> = _perluTindakanItems

    private val _diterimaItems = MutableStateFlow(dummyList.filter {
        it.subStatus == SubStatus.MENDATANG ||
                it.subStatus == SubStatus.PROGRESS ||
                it.subStatus == SubStatus.SELESAI
    })
    val diterimaItems: StateFlow<List<CsrItem>> = _diterimaItems
}