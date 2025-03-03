package com.mathroda.dashcoin.presentation.coin_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.mathroda.dashcoin.R
import com.mathroda.dashcoin.presentation.coin_detail.components.*
import com.mathroda.dashcoin.presentation.coin_detail.viewmodel.CoinViewModel
import com.mathroda.dashcoin.presentation.dialog_screen.CustomDialog
import com.mathroda.dashcoin.presentation.ui.theme.DarkGray
import com.mathroda.dashcoin.presentation.ui.theme.LighterGray
import com.mathroda.dashcoin.presentation.ui.theme.Twitter
import com.mathroda.dashcoin.presentation.watchlist_screen.events.WatchListEvents
import com.mathroda.dashcoin.presentation.watchlist_screen.viewmodel.WatchListViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun CoinDetailScreen(
    coinViewModel: CoinViewModel = hiltViewModel(),
    watchListViewModel: WatchListViewModel = hiltViewModel(),
    navController: NavController
) {

    val coinState = coinViewModel.coinState.value
    var isFavorite by remember { mutableStateOf(false) }
    val lottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_main))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComp,
        iterations = LottieConstants.IterateForever,
    )



    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize()
            .padding(12.dp)
    ) {
       coinState.coin?.let { coin ->
           /**
            * first thing when the coin detail screen renders it checks
            * if the coin exist in Firestore and collect the flow as State
            */
           watchListViewModel.isFavoriteState(coin)
           val isFav = watchListViewModel.isFavoriteState.collectAsState()
           LazyColumn(
               modifier = Modifier
                   .fillMaxSize()
           ) {
               item {
                   /**
                    * here it checks if the rank of current coin is not equal to 0
                    * that means it does exist in Firestore and should assign it value
                    * to true
                    */
                   isFavorite = isFav.value.rank != 0
                   val openDialogCustom = remember{ mutableStateOf(false) }
                   TopBarCoinDetail(
                       coinSymbol = coin.symbol!!,
                       icon = coin.icon!!,
                       navController = navController,
                       isFavorite = isFavorite,
                       onCLick = {
                           isFavorite = !isFavorite

                           if (isFavorite){
                               watchListViewModel.onEvent(WatchListEvents.AddCoin(coin))
                           } else openDialogCustom.value = true
                       }
                   )
                   if (openDialogCustom.value){
                       CustomDialog(
                           openDialogCustom = openDialogCustom,
                           coinName = coin.name!!,
                           coin = coin,
                           navController = navController
                       )
                   }
                   CoinDetailSection(
                       price = coin.price!!,
                       priceChange = coin.priceChange1d!!
                   )

                   Chart(
                       oneDayChange = coin.priceChange1d,
                       context = LocalContext.current
                   )

                   CoinInformation(
                       modifier = Modifier
                           .fillMaxWidth()
                           .background(
                               color = LighterGray,
                               shape = RoundedCornerShape(25.dp)
                           )
                           .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                       rank = "${coin.rank}",
                       volume = numbersToCurrency(coin.volume!!.toInt())!!,
                       marketCap = numbersToCurrency(coin.marketCap!!.toInt())!!,
                       availableSupply = "${numbersToFormat(coin.availableSupply!!.toInt())} ${coin.symbol}" ,
                       totalSupply = "${numbersToFormat(coin.totalSupply!!.toInt())} ${coin.symbol}"
                   )

                   val uriHandler = LocalUriHandler.current
                   Row (
                       horizontalArrangement = Arrangement.Center
                           ){
                       LinkButton(
                           title = "Twitter",
                           modifier = Modifier
                               .padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                               .clip(RoundedCornerShape(35.dp))
                               .height(45.dp)
                               .background(Twitter)
                               .weight(1f)
                               .clickable {
                                   uriHandler.openUri(coin.twitterUrl!!)
                               }
                       )

                       LinkButton(
                           title = "Website",
                           modifier = Modifier
                               .padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                               .clip(RoundedCornerShape(35.dp))
                               .height(45.dp)
                               .background(LighterGray)
                               .weight(1f)
                               .clickable {
                                   uriHandler.openUri(coin.websiteUrl!!)
                               }
                       )
                   }
               }
           }
       }

        if (coinState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                LottieAnimation(
                    composition = lottieComp,
                    progress = { lottieProgress },
                )
            }
        }

        if(coinState.error.isNotBlank()) {
            Text(
                text = coinState.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

fun numbersToCurrency(number: Int): String? {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.currency = Currency.getInstance("USD")
    return numberFormat.format(number)
}

fun numbersToFormat(number: Int): String? {
    val numberFormat = NumberFormat.getNumberInstance()
    return numberFormat.format(number)
}


