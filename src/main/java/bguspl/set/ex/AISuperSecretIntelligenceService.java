package bguspl.set.ex;

import java.util.Random;

import bguspl.set.Env;

public class AISuperSecretIntelligenceService{

    private enum IntelligenceStrength{
        weak,
        medium,
        shabac,
        illuminati
    }

    private static final IntelligenceStrength intelligenceStrength = IntelligenceStrength.illuminati;

    int[][][] sets;
    int cardsCount = 12;

    int isSetTries;
    int isPotentialSetTries;

    public int WAIT_BETWEEN_INTELLIGENCE_GATHERING;

    public int AI_WAIT_BETWEEN_KEY_PRESSES;


    public AISuperSecretIntelligenceService(Env env){
        sets = new int[cardsCount][cardsCount][cardsCount];

        AI_WAIT_BETWEEN_KEY_PRESSES = env.config.penaltyFreezeMillis == 0 ? 50 : 1000;

        switch(intelligenceStrength){
            case weak:{
                isSetTries = 2;
                isPotentialSetTries = 5;
                WAIT_BETWEEN_INTELLIGENCE_GATHERING = 100;
                break;
            }
            case medium:{
                isSetTries = 5;
                isPotentialSetTries = 10;
                WAIT_BETWEEN_INTELLIGENCE_GATHERING = 50;
                break;
            }
            case shabac:{
                isSetTries = 10;
                isPotentialSetTries = 20;
                WAIT_BETWEEN_INTELLIGENCE_GATHERING = 25;
                break;
            }
            case illuminati:{
                isSetTries = 100;
                isPotentialSetTries = 200;
                WAIT_BETWEEN_INTELLIGENCE_GATHERING = 10;
                break;
            }
        }

    }

    private boolean isSet(int i, int j, int k){
        return sets[i][j][k] == 1;
    }

    private boolean isPotentialSet(int i, int j, int k){
        return sets[i][j][k] == 0;
    }

    public void sendIntel(Integer[] cards,boolean truthValue){

        int value = truthValue ? 1:-1;

        for(int i = 0; i < 3 ;i ++){
            sets[cards[(i)%3]][cards[(i+1)%3]][cards[(i+2)%3]] = value;  // 0,1,2 -> 1,2,0 -> 2,0,1
            sets[cards[(i+1)%3]][cards[(i)%3]][cards[(i+2)%3]] = value; // 1,0,2 -> 2,1,0 -> 0,2,1
        }
    }

    public void reportSetClaimed(Integer[] cards) {

        //reportSetClaimed costs exactly 3630 operations.....
        //small price to pay for high quality intelligence...

        for(Integer card : cards){
            for(int i = 0; i < cardsCount ;i ++){
                if(i == card) continue;
                for(int j = 0; j < cardsCount ;j ++){
                    if(j == card) continue;

                    sets[card][i][j] = 0;
                    sets[i][card][j] = 0;
                    sets[i][j][card] = 0;
                    sets[i][card][j] = 0;
                    sets[j][i][card] = 0;
                    sets[card][j][i] = 0;               
                }
            }
        }
    }

    public Integer[] drawPotentialSet(){
        Random rand = new Random();
        int i,j,k;
        int tries = 0;
        do{
            i = rand.nextInt(cardsCount);
            j = rand.nextInt(cardsCount);
            while(i == j) j = rand.nextInt(cardsCount);
            k = rand.nextInt(cardsCount);
            while(k == i | k == j) k = rand.nextInt(cardsCount);
        }while(isPotentialSet(i, j,k) == false & tries++ <= isPotentialSetTries);
        return new Integer[]{i,j,k};
    } 

    public Integer[] getIntel(){
        Random rand = new Random();
        int i,j,k;
        int tries = 0;

        do{
            i = rand.nextInt(cardsCount);
            j = rand.nextInt(cardsCount);
            while(i == j) j = rand.nextInt(cardsCount);
            k = rand.nextInt(cardsCount);
            while(k == i | k == j) k = rand.nextInt(cardsCount);
            tries++;
        }while((isSet(i, j,k) == false & tries <= isSetTries));
        
        if(isSet(i, j,k) == false){
            do{
                i = rand.nextInt(cardsCount);
                j = rand.nextInt(cardsCount);
                while(i == j) j = rand.nextInt(cardsCount);
                k = rand.nextInt(cardsCount);
                while(k == i | k == j) k = rand.nextInt(cardsCount);
                tries++;
            }while((isPotentialSet(i, j,k) == false & tries <= isPotentialSetTries));
        }

        boolean announce_to_console = false;
        if(announce_to_console){
            if(sets[i][j][k] == 1) System.out.println(Thread.currentThread().getName()+" Got intel about a confirmed set!");
            if(sets[i][j][k] == 0) System.out.println(Thread.currentThread().getName()+" Got intel about a potential set!");
            if(sets[i][j][k] == -1) System.out.println(Thread.currentThread().getName()+" Got intel about a non-set!");
        }

        return new Integer[]{i,j,k};
    }
}