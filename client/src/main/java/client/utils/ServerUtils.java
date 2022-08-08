/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

/**
 * This class contains all methods used for communication between the client and
 * server from the client's side.
 */
public class ServerUtils {
    private static String SERVER;
    public static Player player;

    /**
     * The StompSession through which WebSocket connection will be established.
     */
    private static StompSession session;

    private static StompSession.Subscription sub;

    /**
     * Sets the IP of the server that will be used in this game. Since the stomp
     * session is used by WebSockets for the exact same server, this method also
     * connects to the WebSockets and enables bi-directional communication between
     * the server with the specified IP and this client.
     *
     * @param IP - the IP of the server
     */
    public void setServerIPAndStompSession(String IP) {
        SERVER = "http://" + IP + ":8080/";
        session = connect("ws://" + IP + ":8080/websocket");
    }

    /**
     * Requests the entrance into the WebSocket protocol from the server.
     *
     * @param url the url that the request is made to
     * @return a new StompSession through which the bi-directional WebSocket
     *         communication will flow
     */
    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException("Execution exception thrown");
        }
        throw new IllegalStateException();
    }

    /**
     * Called from within the MultiPlayerCtrl, this method makes the client
     * subscribe to the given url (dest) in order to receive Sendable objects.
     *
     * @param dest the url this client subscribes to
     * @param consumer lambda function to be executed when a new object is
     *                 received
     */
    public void registerForSendables(String dest, Consumer<Sendable> consumer) {
        sub = session.subscribe(dest, new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Sendable.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((Sendable) payload);
            }

        });
    }

    /**
     * Ends the subscription to Sendables.
     */
    public void unsubscribe() {
        sub.unsubscribe();
    }

    /**
     * This method is called whenever the client clicks on an emoji
     * button to send an emoji to the other players. It sends an
     * Object e (an emoji) through the WebSocket to the server, from
     * where it is broadcast to all subscribers on the given url
     * (including this client.)
     *
     * @param dest the url to send this emoji to
     * @param e the object (emoji) to send over the WebSocket
     */
    public void send(String dest, Object e) {
        session.send(dest, e);
    }

    /**
     * Tests whether a connection to the server can be established.
     * 
     * @return True if a connection can be established, False if not
     */
    public boolean testConnection () {
        try {
            var url = new URL(SERVER);
            var is = url.openConnection().getInputStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Tests whether a connection to the server can be established.
     * 
     * @param address the addres to try and connect to
     * @return True if a connection can be established, False if not
     */
    public boolean testConnection (String address) {
        try {
            var url = new URL("http://" + address + ":8080/");
            var is = url.openConnection().getInputStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    /**
     * Starts long polling to keep the table with the players in the waiting room up-to-date
     *
     * @param consumer - lambda function to be executed when an update is received
     */
    public void registerForUpdates(Consumer<PlayerUpdate> consumer) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig())
                        .target(SERVER).path("updates")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);
                if (res.getStatus() == 204) continue;
                var p = res.readEntity(PlayerUpdate.class);
                consumer.accept(p);
            }
        });
    }

    /**
     * Stops the thread.
     */
    public void stop() {
        EXEC.shutdownNow();
    }

    /**
     * Gets all players in the waiting room
     *
     * @return a list of all players in the waiting room
     */
    public List<Player> getWaitingPlayers() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/waitingRoom")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Player>>() {});
    }
    /**
     * Adds a new player to the waiting room
     *
     * @return the gameID of the game which the player was added to
     */
    public int addPlayer(String playerName) {
        player = new Player(playerName, 0);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/waitingRoom")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(playerName, APPLICATION_JSON), Integer.class);
    }

    /**
     * Deletes a player from the waiting room
     */
    public void deletePlayer() {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/waitingRoom")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(player, APPLICATION_JSON), Player.class);
        player = null;
    }

    /**
     * Starts the game
     */
    public void start() {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/waitingRoom/start")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Double.class);
    }

    /**
     * Method for getting the next question in a single player game
     * @param gameID the gameID
     * @param index the index of the question
     * @return the next question
     */
    public Question getQuestion(int gameID, int index) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/questions/" + gameID + "/" + index)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Question>() {});
    }

    /**
     * Fetching the image from the server using the PublicController api endpoint
     * @param image_path the path of the image
     * @return an InputStream that is going to be converted to the actual image on the client side
     */
    public InputStream getImage(String image_path) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/" + image_path)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(new GenericType<Response>() {});
        return response.readEntity(InputStream.class);
    }

    /**
     * This method is called at the end of a singleplayer game automatically.
     * It takes the current player's name and their score and posts them to
     * the database as a LeaderboardEntry.
     *
     * @param player the player whose score is to be posted to the database
     * @return the LeaderboardEntry that has been posted
     */
    public LeaderboardEntry postEntryToLeaderboard(Player player) {
        var entry = new LeaderboardEntry(player.getName(), player.getScore());
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/leaderboard/add")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(entry, APPLICATION_JSON), LeaderboardEntry.class);
    }

    /**
     * Called when the user attempts to delete all leaderboard entries with
     * a username and score matching the specified ones. Deletes all such
     * entries from the database.
     *
     * @param username the username whose entries are to be deleted
     * @param score the score entries of the specified user containing
     *
     * @return boolean indicating whether any entries had been deleted
     */
    public boolean deleteEntriesByNameAndScore(String username, int score) {
        if (getEntriesByNameAndScore(username, score).size() == 0)
            return false;
        ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/leaderboard/delete/" + username + "/" + score)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
        return true;
    }

    /**
     * This method checks whether there are any entries in the leaderboard matching
     * the specified username and score pair.
     *
     * @param username the username to check the leaderboard for
     * @param score the score to check the leaderboard for
     *
     * @return a list of all entries found that match the parameters
     */
    public List<LeaderboardEntry> getEntriesByNameAndScore(String username, int score) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/leaderboard/get/" + username + "/" + score)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<LeaderboardEntry>>() {
            });
    }

    /**
     * Sends a player to the server side, so it can be added to the right MPGame and adds its score
     * to the list of score.
     * @param gameID the id of the right game.
     * @param player the player whose score needs to be added.
     * @return - returns the player that has been posted.
     */
    public Player addScore(int gameID, Player player) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/game/add/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(player, APPLICATION_JSON), Player.class);
    }

    /**
     * Sends a player to the server side, so it can be added to the right MPGame and update the
     * score a final time.
     * @param gameID the id of the right game.
     * @param player the player whose score needs to be updated.
     * @return - returns the player that has been posted.
     */
    public Player updateScore(int gameID, Player player) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/game/update/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(player, APPLICATION_JSON), Player.class);
    }

    /**
     * Gets all the entries in the leaderboard database.
     * @return - returns a list of LeaderboardEntries.
     */
    public List<LeaderboardEntry> getAllLeaderboardEntries() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/leaderboard")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<LeaderboardEntry>>() {
                });
    }

    /**
     * Getting the next question in a multiplayer game
     * It uses the api endpoint from the MutltiGameController on server side
     * @param gameID the id of the game
     * @param questionNumber the index of the next Question
     * @return the next Question
     */
    public Question getQuestionGame(int gameID, int questionNumber) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/game/" + gameID + "/" + questionNumber)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Question>() {});
    }

    /**
     * gets a game with the corresponding gameID from the server side.
     * @param gameID the id of the MPGame that is needed.
     * @return - returns an MPGame with the corresponding gameID.
     */
    public MPGame getSpecificGame(int gameID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/game/unique/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<MPGame>() {});
    }

    /**
     * gets a boolean value for the server that indicates if there are any multiplayer games in the
     * list of multiplayer games.
     * @return - returns a boolean value.
     */
    public boolean isEmpty() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/game/isEmpty")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Boolean>() {});
    }        

    /**
     * Puts the request of a client to decrease the time for all the others in a mutiplayer game
     * on the server
     *
     * @param gameID the id of the game for which the time will be halved
     * @param player the player that clicked on the time Joker
     */
    public void halveTime(int gameID, Player player) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/game/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(player, APPLICATION_JSON), Player.class);
    }

    /**
     * Gets the number of players in this game
     *
     * @param gameID the ID of the game
     * @return the number of players
     */
    public int getNumberOfPlayers(int gameID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/game/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Integer>() {});
    }

    /**
     * Getting all activities from the Activity repository
     * It uses the api endpoint from ActivityController on the server-side
     * @return List<Activity> containing all activities
     */
    public List<Activity> getAllActivities() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/activities")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Activity>>() {});
    }

    /**
     * Adding a new activity (or updating an existing one to the Activity repository
     * It uses the api endpoint from ActivityController on the server-side
     * @param activity that needs to be added to the repository
     * @return List<Activity> containing all activities
     */
    public Activity addActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/activities/add")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    /**
     * Uploads a file with a given path to the server
     * 
     * @param file the {@link File} to upload to the server
     * @param path the path where the file should be placed on the server
     * @return a {@link String} containing the name of the placed file
     * @throws IOException when the given file can't be read
     */
    public String addFile(File file, String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/" + path)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(bytes, APPLICATION_OCTET_STREAM), String.class);
    }

    /**
     * Deleting an activity to the Activity repository
     * It uses the api endpoint from ActivityController on the server-side
     * @param activity that needs to be deleted from the repository
     */
    public void deleteActivity(Activity activity) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/activities/delete/" + activity.getActivityID())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }
}