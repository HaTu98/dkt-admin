package vn.edu.vnu.uet.dktadmin.common.utilities;

import vn.edu.vnu.uet.dktadmin.rest.model.PageBaseRequest;

public class PageUtil {
    private static final Integer MAX_SIZE = 100;
    private static final Integer DEFAULT_SIZE = 10;
    private static final Integer DEFAULT_PAGE = 1;

    public static PageBaseRequest validate(PageBaseRequest pageBaseRequest) {
        if (pageBaseRequest == null) {
            pageBaseRequest = new PageBaseRequest(DEFAULT_PAGE, DEFAULT_SIZE);
        } else {
            if (pageBaseRequest.getSize() == null) {
                pageBaseRequest.setSize(DEFAULT_SIZE);
            } else if (pageBaseRequest.getSize() > MAX_SIZE) {
                pageBaseRequest.setSize(MAX_SIZE);
            }
            if (pageBaseRequest.getPage() == null) {
                pageBaseRequest.setPage(DEFAULT_PAGE);
            }
        }
        return pageBaseRequest;
    }
}
