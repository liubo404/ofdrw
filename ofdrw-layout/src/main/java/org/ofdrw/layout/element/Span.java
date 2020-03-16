package org.ofdrw.layout.element;

import org.ofdrw.layout.Rectangle;

import java.util.LinkedList;
import java.util.List;

/**
 * 字体基础单元
 * <p>
 * 用来设置字体样式等
 *
 * @author 权观宇
 * @since 2020-02-03 02:01:53
 */
public class Span {

    /**
     * 字体
     */
    private Font font;

    /**
     * 字体大小
     */
    private Double fontSize;

    /**
     * 字间距
     * <p>
     * 默认为 0
     */
    private Double letterSpacing = 0d;

    /**
     * 是否加粗
     * <p>
     * 默认不加粗 false
     */
    private boolean bold = false;

    /**
     * 是否斜体
     * <p>
     * 默认非斜体 false
     */
    private boolean italic = false;

    /**
     * 是否含有下划线
     * <p>
     * 默认不含下划线
     */
    private boolean underline = false;

    /**
     * 文本内容
     */
    private String text;

    /**
     * 当渲染空间不足时可能会拆分元素
     * <p>
     * true为不拆分，false为拆分。默认值为false
     */
    private Boolean integrity = false;

    private LinkedList<TxtGlyph> txtGlyphsCache;

    private Span() {
        this.setFont(Font.getDefault());
    }

    public Span(Font font, Double fontSize, String text) {
        this.font = font;
        this.fontSize = fontSize;
        setText(text);
    }

    public Span(String text) {
        this();
        if (text == null) {
            throw new IllegalArgumentException("text内容为空");
        }
        setText(text);
    }

    /**
     * @return 字符数量
     */
    public int length() {
        return text.length();
    }

    public Font getFont() {
        return font;
    }

    public Span setFont(Font font) {
        this.font = font;
        return this;
    }

    public Double getFontSize() {
        return fontSize;
    }

    public Span setFontSize(Double fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public Double getLetterSpacing() {
        return letterSpacing;
    }

    public Span setLetterSpacing(Double letterSpacing) {
        this.letterSpacing = letterSpacing;
        return this;
    }

    public boolean isBold() {
        return bold;
    }

    public Span setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public boolean isItalic() {
        return italic;
    }

    public Span setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public boolean isUnderline() {
        return underline;
    }

    public Span setUnderline(boolean underline) {
        this.underline = underline;
        return this;
    }

    public String getText() {
        return text;
    }

    public Span setText(String text) {
        this.text = text;
        if (txtGlyphsCache != null) {
            // 如果已经存在缓存，那么重新建立缓存
            glyphList();
        }
        return this;
    }

    /**
     * 元素是否可以拆分
     *
     * @return true 可以拆分；false 不能拆分
     */
    public Boolean isIntegrity() {
        return integrity;
    }

    public Span setIntegrity(Boolean integrity) {
        this.integrity = integrity;
        return this;
    }

    /**
     * 获取字体图形列表
     *
     * @return 字体图形列表
     */
    public List<TxtGlyph> glyphList() {
        if (txtGlyphsCache == null) {
            txtGlyphsCache = new LinkedList<>();
            for (char c : this.text.toCharArray()) {
                txtGlyphsCache.add(new TxtGlyph(c, this));
            }
        }
        return txtGlyphsCache;
    }

    /**
     * @return 不拆分情况下都放在一行内占用的大小
     */
    public Rectangle blockSize() {
        List<TxtGlyph> txtGlyphs = glyphList();
        double width = 0;
        double height = 0;
        for (TxtGlyph glyph : txtGlyphs) {
            width += glyph.getW();
            height = glyph.getH();
        }
        return new Rectangle(width, height);
    }

    /**
     * 切分元素
     *
     * @param index 字符坐标
     * @return 切分后的两个全新元素
     */
    public Span[] split(int index) {
        if (index < 0 || index >= text.length()) {
            throw new IllegalArgumentException("非法的切分数组坐标(index): " + index);
        }
        Span s1 = this.clone();
        s1.text = this.text.substring(0, index);
        Span s2 = this.clone();
        s2.text = this.text.substring(index);
        return new Span[]{s1, s2};
    }

    @Override
    public Span clone() {
        Span span = new Span();
        span.font = font;
        span.fontSize = fontSize;
        span.letterSpacing = letterSpacing;
        span.bold = bold;
        span.italic = italic;
        span.underline = underline;
        span.text = new String(text);
        span.integrity = integrity;
        return span;
    }
}