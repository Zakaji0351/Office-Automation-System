import com.lzq.oa.utils.MyBatisUtils;
import org.junit.Test;

public class MyBatisUtilsTestor {
    @Test
    public void testCase1(){
        MyBatisUtils.executeQuery(sqlSession -> {
            String out = (String)sqlSession.selectOne("test.sample");
            return out;
        });
    }
    @Test
    public void testCase2(){
        String result = (String)MyBatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("test.sample"));
        System.out.println(result);
    }
}
