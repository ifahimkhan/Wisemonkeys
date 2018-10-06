package in.wisemonkeys.android.wisemonkeys.usermodel;

/**
 * Created by HSBC on 21-10-2017.
 */

class Meta {
    public String meta_key ;
    public String meta_value ;

    public Meta(String meta_key, String meta_value) {
        this.meta_key = meta_key;
        this.meta_value = meta_value;
    }

    public Meta() {
    }

    public String getMeta_key() {
        return meta_key;
    }

    public void setMeta_key(String meta_key) {
        this.meta_key = meta_key;
    }

    public String getMeta_value() {
        return meta_value;
    }

    public void setMeta_value(String meta_value) {
        this.meta_value = meta_value;
    }
}
