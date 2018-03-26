package utils;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;


public class JsonBean implements IPickerViewData {

    /**
     * name:省份：福建
     * city:[{"name":"厦门市","area":["思明","湖里"}]
     */
    private String name;
    private List<CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }


    /**
     * 实现 IPickerViewData 接口
     * 这个用来显示在PickerView上面的字符串
     * PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
     */
    public String getPickerViewText() {
        return this.name;
    }


    /**
     * name:城市名：厦门
     * area:区：思明区
     */
    public static class CityBean {
        private String name;
        private List<String> area;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getArea() {
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }
    }
}
