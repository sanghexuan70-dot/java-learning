
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

public class HutoolDemo {
    public static void main(String[] args){
        System.out.println(StrUtil.isBlank("    "));
        System.out.println(DateUtil.now());
    }
}
