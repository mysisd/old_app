package utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class GuPiao {

    /**
     * data : {"sort":{"fields":["iopv","current_amount","last_px","vol_ratio","dyn_pb_rate","amplitude","min5_chgpct","wavg_px","prod_name","shares_per_hand","debt_fund_value","market_value","bps","amount","turnover_ratio","entrust_rate","entrust_diff","circulation_amount","circulation_value","eps","prev_amount","preclose_px","market_date","high_px","low_px","business_amount","business_count","business_balance","open_px","bid_grp","offer_grp","trade_status","data_timestamp","up_px","down_px","business_amount_in","business_amount_out","w52_low_px","w52_high_px","px_change","px_change_rate","trade_mins","total_shares","pe_rate","business_balance_scale"],"519508.SS":[0,0,97.02,0,0,0,-14.27,0,"万家货A ",100,97020,0,0,0,0,0,0,0,0,0,0,113.17,20180314,0,0,0,0,0,0,"0.000,0,0,0.000,0,0,0.000,0,0,0.000,0,0,0.000,0,0,","0.000,0,0,0.000,0,0,0.000,0,0,0.000,0,0,0.000,0,0,","HALT",90521000,0,0,0,0,78.69,1069.52,-16.15,-14.27,0,0,0,0]}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sort : {"fields":["iopv","current_amount","last_px","vol_ratio","dyn_pb_rate","amplitude","min5_chgpct","wavg_px","prod_name","shares_per_hand","debt_fund_value","market_value","bps","amount","turnover_ratio","entrust_rate","entrust_diff","circulation_amount","circulation_value","eps","prev_amount","preclose_px","market_date","high_px","low_px","business_amount","business_count","business_balance","open_px","bid_grp","offer_grp","trade_status","data_timestamp","up_px","down_px","business_amount_in","business_amount_out","w52_low_px","w52_high_px","px_change","px_change_rate","trade_mins","total_shares","pe_rate","business_balance_scale"],"519508.SS":[0,0,97.02,0,0,0,-14.27,0,"万家货A ",100,97020,0,0,0,0,0,0,0,0,0,0,113.17,20180314,0,0,0,0,0,0,"0.000,0,0,0.000,0,0,0.000,0,0,0.000,0,0,0.000,0,0,","0.000,0,0,0.000,0,0,0.000,0,0,0.000,0,0,0.000,0,0,","HALT",90521000,0,0,0,0,78.69,1069.52,-16.15,-14.27,0,0,0,0]}
         */

        private SortBean sort;

        public SortBean getSort() {
            return sort;
        }

        public void setSort(SortBean sort) {
            this.sort = sort;
        }

        public static class SortBean {
            private List<String> fields;
            @SerializedName("519508.SS")
            private List<Integer> _$_519508SS118; // FIXME check this code

            public List<String> getFields() {
                return fields;
            }

            public void setFields(List<String> fields) {
                this.fields = fields;
            }

            public List<Integer> get_$_519508SS118() {
                return _$_519508SS118;
            }

            public void set_$_519508SS118(List<Integer> _$_519508SS118) {
                this._$_519508SS118 = _$_519508SS118;
            }
        }
    }
}
