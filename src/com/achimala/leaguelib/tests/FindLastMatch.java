package com.achimala.leaguelib.tests;

import com.achimala.leaguelib.errors.LeagueException;

import java.io.IOException;
import com.achimala.leaguelib.connection.*;
import com.achimala.leaguelib.models.*;
import com.achimala.leaguelib.errors.*;
import com.achimala.util.Callback;

import java.util.Iterator;
import java.util.Map;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 11/22/13
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class FindLastMatch {
    private static int count = 0;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition done = lock.newCondition();

    private static void incrementCount() {
        lock.lock();
        count++;
        lock.unlock();
    }

    private static void decrementCount() {
        lock.lock();
        count--;
        if(count == 0)
            done.signal();
        lock.unlock();
    }

    public static void main(String[] args) throws IOException, LeagueException, InterruptedException {
        final LeagueConnection c = new LeagueConnection(LeagueServer.NORTH_AMERICA);
        c.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.NORTH_AMERICA, "3.14.xx", "imsixpool", args[0]));
        final String SUMMONER_TO_LOOK_UP = "isixpool";

        Map<LeagueAccount, LeagueException> exceptions = c.getAccountQueue().connectAll();
        if(exceptions != null) {
            for(LeagueAccount account : exceptions.keySet())
                System.out.println(account + " error: " + exceptions.get(account));
            return;
        }

        lock.lock();
        incrementCount();
        c.getSummonerService().getSummonerByName(SUMMONER_TO_LOOK_UP, new Callback<LeagueSummoner>() {
            public void onCompletion(LeagueSummoner summoner) {
                lock.lock();

                System.out.println(summoner.getName() + ":");
                System.out.println("    accountID:  " + summoner.getAccountId());
                System.out.println("    summonerID: " + summoner.getId());

                incrementCount();
                System.out.println("Getting profile data...");
                c.getSummonerService().fillPublicSummonerData(summoner, new Callback<LeagueSummoner>() {
                    public void onCompletion(LeagueSummoner summoner) {
                        c.getPlayerStatsService().fillMatchHistory(summoner, new Callback<LeagueSummoner>() {
                            public void onCompletion(LeagueSummoner summoner) {
                                MatchHistoryEntry last_match = summoner.getMostRecentMatch();
                                System.out.println(last_match.getChampionSelectionForSummoner(summoner));
                                Map stats = last_match.getAllStats();
                                Iterator iterator = stats.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry mapEntry = (Map.Entry) iterator.next();
                                    System.out.println(mapEntry.getKey()
                                            + ": " + mapEntry.getValue());
                                }
                            }

                            public void onError(Exception ex) {
                            }
                        });
                    }
                    public void onError(Exception ex) {
                    }
                });
                incrementCount();
                System.out.println("Getting leagues data...");
                c.getLeaguesService().fillSoloQueueLeagueData(summoner, new Callback<LeagueSummoner>() {
                    public void onCompletion(LeagueSummoner summoner) {
                        lock.lock();
                        LeagueSummonerLeagueStats stats = summoner.getLeagueStats();
                        if(stats != null) {
                            System.out.println("League:");
                            System.out.println("    Name: " + stats.getLeagueName());
                            System.out.println("    Tier: " + stats.getTier());
                            System.out.println("    Rank: " + stats.getRank());
                            System.out.println("    Wins: " + stats.getWins());
                            System.out.println("    ~Elo: " + stats.getApproximateElo());
                        } else {
                            System.out.println("NOT IN LEAGUE");
                        }
                        System.out.println();
                        System.out.flush();
                        decrementCount();
                        lock.unlock();
                    }

                    public void onError(Exception ex) {
                        lock.lock();
                        System.out.println(ex.getMessage());
                        decrementCount();
                        lock.unlock();
                    }
                });

                decrementCount();
                lock.unlock();
            }
            public void onError(Exception ex) {
                lock.lock();
                ex.printStackTrace();
                decrementCount();
                lock.unlock();
            }
        });

        System.out.println("Out here, waiting for it to finish");
        done.await();
        // c.getInternalRTMPClient().join();
        System.out.println("Client joined, terminating");
        lock.unlock();
        System.exit(1);
    }
}
