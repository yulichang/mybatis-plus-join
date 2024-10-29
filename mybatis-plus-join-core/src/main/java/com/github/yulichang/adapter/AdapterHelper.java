package com.github.yulichang.adapter;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.github.yulichang.adapter.base.IAdapter;
import com.github.yulichang.adapter.base.tookit.VersionUtils;
import com.github.yulichang.adapter.v312.Adapter312;
import com.github.yulichang.adapter.v33x.Adapter33x;
import com.github.yulichang.adapter.v3431.Adapter3431;
import com.github.yulichang.adapter.v355.Adapter355;
import lombok.Getter;

import java.util.Optional;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class AdapterHelper {

    @Getter
    private static IAdapter adapter;


    static {
        String lastAdapter = "3.5.9";
        String version = Optional.ofNullable(VersionUtils.getVersion()).orElse(lastAdapter);

        if (VersionUtils.compare(version, "3.5.6") >= 0) {
            adapter = new Adapter();
        } else if (VersionUtils.compare(version, "3.5.4") >= 0) {
            adapter = new Adapter355();
        } else if (VersionUtils.compare(version, "3.4.0") >= 0) {
            adapter = new Adapter3431();
        } else if (VersionUtils.compare(version, "3.3.0") >= 0) {
            adapter = new Adapter33x();
        } else if (VersionUtils.compare(version, "3.1.2") >= 0) {
            adapter = new Adapter312();
        } else {
            throw ExceptionUtils.mpe("MPJ需要MP版本3.1.2+，当前MP版本%s", version);
        }
    }

    public static void setAdapter(IAdapter adapter) {
        AdapterHelper.adapter = adapter;
    }
}
