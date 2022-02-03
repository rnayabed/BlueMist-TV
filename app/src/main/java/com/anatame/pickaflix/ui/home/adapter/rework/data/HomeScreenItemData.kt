package com.anatame.pickaflix.ui.home.adapter.rework.data

import com.anatame.pickaflix.model.HomeScreenData
import com.anatame.pickaflix.ui.home.HomeViewModel
import com.anatame.pickaflix.ui.home.adapter.rework.providers.HomeItemProvider
import com.anatame.pickaflix.ui.home.adapter.rework.items.MovieCategoryItem
import com.anatame.pickaflix.ui.home.adapter.rework.items.ViewPagerItem
import com.anatame.pickaflix.ui.home.adapter.rework.items.WatchListItem
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
            WatchListItem(
                homeScreenData.watchList,
                "Watchlist",
                "Continue Watching",
                homeViewModel.homeItemScrollStates.scrollStateWatchList,
            )
        )
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(0, 23),
                "Recommended",
                "",
                homeViewModel.homeItemScrollStates.scrollState1,
            )
        )
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(24, 47),
                "TV Shows",
                "",
                homeViewModel.homeItemScrollStates.scrollState2,
            )
        )
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(48, 71),
                "Trending",
                "",
                homeViewModel.homeItemScrollStates.scrollState3,
            )
        )
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(72, 87),
                "Latest Movies",
                "",
                homeViewModel.homeItemScrollStates.scrollState4,
            )
        )
        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(88, 103),
                "Latest TV-Series",
                "",
                homeViewModel.homeItemScrollStates.scrollState5,
            )
        )

        repoData.add(
            MovieCategoryItem(
                homeScreenData.movieItems.getMovies(104, 120),
                "Requested",
                "",
                homeViewModel.homeItemScrollStates.scrollState5,
            )
        )
    }
}