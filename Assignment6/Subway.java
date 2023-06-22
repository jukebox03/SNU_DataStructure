import java.io.*;
import java.net.Inet4Address;
import java.util.*;

public class Subway {
    public static Map<String, Integer> station_code_to_order = new HashMap<String, Integer>();
    public static Map<Integer, String> station_order_to_code = new HashMap<Integer, String>();
    public static Map<String, String> station_code_to_name = new HashMap<String, String>();
    public static int [][] distance;
    public static int num = 0;
    public final static int MAX = Integer.MAX_VALUE/2;
    public static int minTime = MAX;
    public static ArrayList<String> minPath = new ArrayList<String>();

    public static void main(String args[]){
        setData(args[0]);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try{
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;
                command(input);
            }
            catch (IOException e){
                System.out.println(e.toString());
            }
        }
    }

    private static void setData(String file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader((file)));
            String line = null;
            num = 0;
            while((line = reader.readLine()) != null && line.length() != 0){
                String station_num = line.split(" ")[0];
                String station_name = line.split(" ")[1];
                station_code_to_name.put(station_num, station_name);
                station_code_to_order.put(station_num, num);
                station_order_to_code.put(num, station_num);
                num++;
            }
            distance = new int[num][num];
            for(int i = 0; i < num; i++){
                for(int j =0; j < num; j++){
                    distance[i][j] = MAX;
                }
            }
            while((line = reader.readLine()) != null && line.length() != 0){
                String start = line.split(" ")[0];
                String end = line.split(" ")[1];
                int start_order = station_code_to_order.get(start);
                int end_order = station_code_to_order.get(end);
                distance[start_order][end_order] = Integer.parseInt(line.split(" ")[2]);
            }
            for(String i : station_code_to_name.keySet()){
                for(String j : station_code_to_name.keySet()){
                    String i_name = station_code_to_name.get(i);
                    String j_name = station_code_to_name.get(j);
                    int i_order = station_code_to_order.get(i);
                    int j_order = station_code_to_order.get(j);
                    if(i_name.equals(j_name) && i.equals(j) == false){
                        distance[i_order][j_order] = 5;
                    }
                }
            }
            while((line = reader.readLine()) != null && line.length() != 0){
                String name = line.split(" ")[0];
                String time = line.split(" ")[1];
                ArrayList<String> key = new ArrayList<String>();

                for(String k : station_code_to_name.keySet()){
                    if(name.equals(station_code_to_name.get(k))){
                        key.add(k);
                    }
                }
                for(String i : key){
                    for(String j : key){
                        int i_order = station_code_to_order.get(i);
                        int j_order = station_code_to_order.get(j);
                        if(i_order != j_order){
                            distance[i_order][j_order] = Integer.parseInt(time);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void command(String input) {
        String start = input.split(" ")[0];
        String end = input.split(" ")[1];
        ArrayList<String> start_code = new ArrayList<String>();
        ArrayList<String> end_code = new ArrayList<String>();

        for(String k : station_code_to_name.keySet()){
            if(start.equals(station_code_to_name.get(k))){
                start_code.add(k);
            }
            if(end.equals(station_code_to_name.get(k))){
                end_code.add(k);
            }
        }
        dijkstra(start_code, end_code);
    }

    private static void dijkstra(ArrayList<String> start_code, ArrayList<String> end_code){
        minPath = new ArrayList<>();
        minTime = MAX;
        for(String start : start_code){
            for(String end : end_code){
                int start_num = station_code_to_order.get(start);
                int end_num = station_code_to_order.get(end);
                int [] prev = new int[num];
                int [] distances = new int[num];
                boolean [] visited = new boolean[num];

                for(int i = 0; i < num; i++){
                    distances[i] = MAX;
                    visited[i] = false;
                }

                distances[start_num] = 0;

                for(int i = 0 ; i < num-1; i++){
                    int minStation = findMinStation(distances, visited);
                    visited[minStation] = true;
                    for(int j=0; j < num; j++){
                        if(visited[j] == false && distances[minStation] != MAX){
                            int newDistance = distances[minStation] + distance[minStation][j];
                            if(newDistance < distances[j]){
                                distances[j] = newDistance;
                                prev[j] = minStation;
                            }
                        }
                    }
                }
                if(minTime>distances[end_num])
                {
                    minTime = distances[end_num];
                    minPath = new ArrayList<>();
                    int current = end_num;
                    while(current != start_num){
                        minPath.add(station_code_to_name.get(station_order_to_code.get(current)));
                        current = prev[current];
                    }
                    minPath.add(station_code_to_name.get(station_order_to_code.get(current)));
                }
            }
        }
        System.out.print(minPath.get(minPath.size()-1));
        for(int i = minPath.size()-2; i > 0; i--){
            if(minPath.get(i).equals(minPath.get(i-1))){
                System.out.print(" ["+minPath.get(i)+"]");
                i--;
            }else{
                System.out.print(" "+minPath.get(i));
            }
        }
        System.out.print(" "+minPath.get(0)+"\r\n");
        System.out.print(minTime+"\r\n");
    }
    private static int findMinStation(int[] distances, boolean[] visited){
        int minStation = -1;
        for(int i = 0; i < distances.length; i++){
            if(visited[i] == false && (minStation == -1 || distances[i] < distances [minStation])){
                minStation = i;
            }
        }
        return minStation;
    }
}
