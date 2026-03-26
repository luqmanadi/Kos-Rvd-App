package com.kosrvd.app.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationGraph{

    val route: String

    @Serializable
    data object AuthGraph : NavigationGraph {
        override val route: String = "auth_graph"
    }

    @Serializable
    data object MainGraph : NavigationGraph {
        override val route: String = "main_graph"
    }

    @Serializable
    data object TagihanGraph : NavigationGraph {
        override val route: String = "tagihan_graph"
    }

    @Serializable
    data object KeluhanGraph : NavigationGraph {
        override val route: String = "keluhan_graph"
    }

    @Serializable
    data object PengumumanGraph : NavigationGraph {
        override val route: String = "pengumuman_graph"
    }

    @Serializable
    data object PenyewaanGraph : NavigationGraph {
        override val route: String = "penyewaan_graph"
    }

    @Serializable
    data object AkunPenggunaGraph : NavigationGraph {
        override val route: String = "akun_pengguna_graph"
    }

    @Serializable
    data object ParkiranHarianMobilGraph : NavigationGraph {
        override val route: String = "parkir_harian_mobil_graph"
    }

    @Serializable
    data object ZonaParkirGraph : NavigationGraph {
        override val route: String = "zona_parkir_graph"
    }

    @Serializable
    data object KamarGraph : NavigationGraph {
        override val route: String = "kamar_graph"
    }

    @Serializable
    data object PenghuniGraph : NavigationGraph {
        override val route: String = "penghuni_graph"
    }
}