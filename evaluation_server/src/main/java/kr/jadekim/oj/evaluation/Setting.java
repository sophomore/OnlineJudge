package kr.jadekim.oj.evaluation;

import com.google.gson.annotations.SerializedName;
import kr.jadekim.oj.evaluation.utils.Logger;

/**
 * Created by jdekim43 on 2016. 2. 27..
 */
public class Setting {

    @SerializedName("debug") public boolean DEBUG = false;
    @SerializedName("resource_path") public String RESOURCE_PATH = "../resources/";
    @SerializedName("evaluation_temp_path") public String TEMP_PATH = "/tmp/oj/";
    public String EVALUATION_TEMP_PATH = TEMP_PATH+"evaluation/";
    @SerializedName("char_set") public String CHAR_SET = "UTF-8";
    @SerializedName("evaluator_count") public int EVALUATOR_COUNT = 2;
    @SerializedName("docker_container_name") public String DOCKER_CONTAINER = "container";

    private static Setting instance;

    private Setting() {}

    public static Setting get() {
        if (instance == null) {
            Logger.warning("not loaded setting. use default setting");
            instance = new Setting();
        }
        return instance;
    }

    static void set(Setting instance) {
        Setting.instance = instance;
    }
}
