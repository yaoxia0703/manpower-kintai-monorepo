package com.manpowergroup.kintai.system.application.service.impl.manager;

import com.manpowergroup.kintai.system.application.dto.manager.response.SubordinateFilterOptionsResponse;
import com.manpowergroup.kintai.system.domain.repository.manager.ManagerSubordinateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ManagerSubordinateServiceImplTest {

    @Test
    void optionsReturnsManagerScopedFilterOptions() {
        ManagerSubordinateRepository repository = Mockito.mock(ManagerSubordinateRepository.class);
        ManagerSubordinateServiceImpl service = new ManagerSubordinateServiceImpl(repository);

        SubordinateFilterOptionsResponse expected = SubordinateFilterOptionsResponse.builder()
                .nodes(List.of(SubordinateFilterOptionsResponse.NodeOption.builder()
                        .id(10L)
                        .parentId(1L)
                        .name("Sales")
                        .code("SALES")
                        .typeCode("DEPT")
                        .level(2)
                        .build()))
                .grades(List.of(SubordinateFilterOptionsResponse.GradeOption.builder()
                        .id(20L)
                        .name("Manager")
                        .code("JP_MANAGER")
                        .gradeLevel("L2")
                        .build()))
                .build();
        when(repository.filterOptions(3L)).thenReturn(expected);

        SubordinateFilterOptionsResponse actual = service.options(3L);

        assertEquals(expected, actual);
        verify(repository).filterOptions(3L);
    }
}
