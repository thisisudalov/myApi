package Entity;

import java.util.List;
import java.util.Map;

public class ResponseGood {
    private String name;
    private long code;
    private Map<String, Integer> rgmap;

    public ResponseGood(Map<String, Integer> rgmap, String name, long code) {
        this.rgmap = rgmap;
        this.name = name;
        this.code = code;
        System.out.println(this);
    }

    public Map<String, Integer> getRgmap() {
        return rgmap;
    }

    public void setRgmap(Map<String, Integer> rgmap) {
        this.rgmap = rgmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String convertWithIteration(Map<String, Integer> map) {
        StringBuilder mapAsString = new StringBuilder("[");
        for (String key : map.keySet()) {
            mapAsString.append("{\"" + key + "\" : \"" + map.get(key) + "\"}, ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("\n]");
        return mapAsString.toString();
    }

    @Override
    public String toString() {
        return "{" +
                "\"rgmap\" : " +convertWithIteration(rgmap) +
                ",\n\"name\" : " + "\""+name +"\""+
                ", \n\"code\" :"+"\"" + code +"\""+
                "\n},";
    }
}
