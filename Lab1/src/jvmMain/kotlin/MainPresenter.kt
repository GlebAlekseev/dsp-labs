import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainPresenter {
    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        CoroutineScope(Dispatchers.IO).launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.Start -> {}
                    is MainIntent.End -> {}
                }
            }
        }
    }
}