package info.ivicel.springflasky.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtil {

    public static List<Integer> pagination(int currentPage, int totalPages) {
        List<Integer> list = new ArrayList<>(totalPages);
        boolean flag = true;
        for (int i = 1; i < totalPages + 1; i++) {
            if (i <= 2 || (i >= currentPage - 2 && i <= currentPage + 5) || i >= totalPages - 2) {
                list.add(i);
                flag = true;
            } else if (flag) {
                list.add(null);
                flag = false;
            }
        }
        return list;
    }

    public static Pageable parsePage(Pageable pageable) {
        return pageable.getPageNumber() > 0 ? PageRequest.of(
                pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort()) :
                pageable;
    }
}
