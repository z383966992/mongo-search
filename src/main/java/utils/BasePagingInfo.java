package utils;
import java.io.Serializable;

public class BasePagingInfo implements Serializable {
    private static final long serialVersionUID = -2111112319022171838L;
    private int pageRows = 10;

    private int pageNum = 1;
    private int offset;
    private int startIdx;
    private int endIdx;
    private int totalRows;
    private int totalPages;

    public int getPageRows() {
        return this.pageRows;
    }

    public void setPageRows(int pageRows) {
        this.pageRows = pageRows;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getOffset() {
        this.offset = (this.pageRows * (this.pageNum - 1));
        return this.offset;
    }

    public int getStartIdx() {
        this.startIdx = (this.pageRows * (this.pageNum - 1) + 1);
        return this.startIdx;
    }

    public int getEndIdx() {
        this.endIdx = (this.startIdx + this.pageRows - 1);
        return this.endIdx;
    }

    public int getTotalRows() {
        return this.totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPages() {
        this.totalPages = ((this.totalRows + this.pageRows - 1) / this.pageRows);
        return this.totalPages;
    }
}