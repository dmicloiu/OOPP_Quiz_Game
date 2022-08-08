package server.api;

import commons.PlayerUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/updates")
public class LongPollingController {
    public final Map<Object, Consumer<PlayerUpdate>> listeners = new HashMap<>();
    /**
     * Long polling to update the clients' waiting rooms with newly joined players
     * and to update the time for the players that are in a game and for that the
     * decreasing-time Joker was clicked
     *
     * @return - the newly added player
     */
    @GetMapping
    public DeferredResult<ResponseEntity<PlayerUpdate>> getUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<PlayerUpdate>>(5000L, noContent);

        var key = new PlayerUpdate();
        listeners.put(key, p -> {
            res.setResult(ResponseEntity.ok(p));
        });
        res.onCompletion(() -> listeners.remove(key));

        return res;
    }

}
