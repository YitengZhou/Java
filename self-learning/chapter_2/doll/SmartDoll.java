/*
* 版权 2005-2025
* 本程序采用GPL协议，你可以从以下网址获得该协议内容
* www.test.com
*/
package doll;
import doll.Doll;

/**
 * <p><strong>SmartDoll</strong> 代表只智能。</p>
 * @author yiteng zhou
 * @version 3.0
 * @since 1.0
 * @see com.abc.dollapp.doll.Doll
 */

public class SmartDoll extends Doll
{
    /**
     * 代表智能
     */
    protected String word;
    /**
     * 构造一个智能
     */
    public SmartDoll(String name)
    {
        super(name);
    }
    /**
     * 构造智能
     * @param word 默认说的话
     */
    public SmartDoll(String name, String word)
    {
        super(name);
        this.word=word;
    }
    /**
     * 获得默认的话
     * @return 返回默认说的话
     * @see #getWord
     * @deprecated 该方法已经被废弃
     */
    public String getWord()
    {
        return this.word;
    }
    /**
     * 设置默认情况下说的话
     * @param word 默认情况下说的话
     * @see #setWord
     * @since 2.0
     */
    public void setWord(String word)
    {
        this.word=word;
    }
    /**
     * <ul>
     * <li> 如果{@link #word word 成员变量}不为null。
     *      就调用{@link#speak(String)speak(String)方法}</li>
     * <li> 如果{@link #word word 成员变量}为null。
     *      就调用{@link com.abc.dollapp.doll#speak()super.speak()方法}</li>
     * </ul>
     */
    public void speak()
    {
        if (this.word!=null)
        {
            try{
                speak(word);
            }catch(Exception e){}
        }
        else{
            super.speak();
        }
    }
    /**
     * @param word 指定智能
     * @return 智能
     * @exception Exception 如果word参数为null，就抛出异常
     */
    public String speak(String word) throws Exception{
        if (word==null)
            throw new Exception("不知道该说啥");
        System.out.println(word);
        return word;
    }
}