package com.example.weather;

import java.util.List;

/**
 * 天气查询API返回结果
 */
public class Weather {
    //值为0或1：1：成功；0：失败
    public int status;//返回状态
    public int count;//返回结果总数目
    public String info;//返回的状态信息
    public int infocode;//返回状态说明,10000代表正确
    public List<Lives> lives;

    public List<Lives> getLives() {
        return lives;
    }

    public void setLives(List<Lives> lives) {
        this.lives = lives;
    }

    public class Lives {
        public String province;//省份名
        public String city;//城市名
        public String adcode;//区域编码
        public String weather;//天气状况
        public String temperature;//温度
        public String winddirection;//风向
        public String windpower; //风力
        public String humidity;//湿度
        public String reporttime;//数据发布的时间

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }


        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }





        public String getWinddirection() {
            return winddirection;
        }

        public void setWinddirection(String winddirection) {
            this.winddirection = winddirection;
        }

        public String getWindpower() {
            return windpower;
        }

        public void setWindpower(String windpower) {
            this.windpower = windpower;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getReporttime() {
            return reporttime;
        }

        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getInfocode() {
        return infocode;
    }

    public void setInfocode(int infocode) {
        this.infocode = infocode;
    }

}

