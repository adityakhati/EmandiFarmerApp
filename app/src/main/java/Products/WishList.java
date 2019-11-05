package Products;

public class WishList {
    public String key;

    public String child;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public WishList(String key, String child) {
        this.key = key;
        this.child = child;
    }
}
