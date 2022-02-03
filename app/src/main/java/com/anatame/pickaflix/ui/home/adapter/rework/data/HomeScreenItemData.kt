package com.anatame.pickaflix.ui.home.adapter.rework.data

import com.anatame.pickaflix.model.HomeScreenData
import com.anatame.pickaflix.ui.home.HomeViewModel
import com.anatame.pickaflix.ui.home.adapter.rework.providers.HomeItemProvider
import com.anatame.pickaflix.ui.home.adapter.rework.items.MovieCategoryItem
import com.anatame.pickaflix.ui.home.adapter.rework.items.ViewPagerItem
import com.anatame.pickaflix.ui.home.adapter.rework.providers.HomeItemDataProvider
import com.anatame.pickaflix.ui.home.category.getMovies

class HomeScreenItemData(
    private val homeScreenData: HomeScreenData,
    private val homeViewModel: HomeViewModel
): HomeItemDataProvider {

    override val repoData = ArrayList<HomeItemProvider>()

    init{

        repoData.add(ViewPagerItem(homeScreenData.sliderItems))
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(0, 23),
                "Brah",
                "",
                homeViewModel.homeItemScrollStates.scrollState1,
            )
        )
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(24, 47),
                "Brah",
                "",
                homeViewModel.homeItemScrollStates.scrollState2,
            )
        )
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(48, 71),
                "Brah",
                "",
                homeViewModel.homeItemScrollStates.scrollState3,
            )
        )
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(72, 95),
                "Brah",
                "",
                homeViewModel.homeItemScrollStates.scrollState4,
            )
        )
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(96, 120),
                "Brah",
                "",
                homeViewModel.homeItemScrollStates.scrollState5,
            )
        )
    }
}