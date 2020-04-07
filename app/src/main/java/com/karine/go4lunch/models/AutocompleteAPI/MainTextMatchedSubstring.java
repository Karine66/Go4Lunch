
package com.karine.go4lunch.models.AutocompleteAPI;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MainTextMatchedSubstring {

    @SerializedName("length")
    private Long mLength;
    @SerializedName("offset")
    private Long mOffset;

    public Long getLength() {
        return mLength;
    }

    public void setLength(Long length) {
        mLength = length;
    }

    public Long getOffset() {
        return mOffset;
    }

    public void setOffset(Long offset) {
        mOffset = offset;
    }

}
