package config.mybatis.auto;

public class LsurlDB_Shortlink extends AbstractGen
{

    @Override
    public String getConfPath()
    {
        return "src/config/mybatis/auto/generatorConfig.xml"; 
    }  
    
    public static void main(String[] args)
    {
        AbstractGen gen = new LsurlDB_Shortlink();
        gen.gen();
    }
}
