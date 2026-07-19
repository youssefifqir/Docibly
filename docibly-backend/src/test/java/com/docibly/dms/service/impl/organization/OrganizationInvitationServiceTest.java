package com.docibly.dms.service.impl.organization;

import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.dao.facade.core.organization.OrganizationInvitationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrganizationInvitationService Unit Tests")
class OrganizationInvitationServiceTest {

    @Mock
    private OrganizationInvitationDao dao;

    @InjectMocks
    private OrganizationInvitationServiceImpl service;

    private OrganizationInvitation sample;

    @BeforeEach
    void setUp() {
        sample = new OrganizationInvitation();
        sample.setId(1L);
        sample.setRef("organizationinvitation-ref-001");
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("returns all entities when records exist")
        void returnsEntities() {
            when(dao.findAll()).thenReturn(List.of(sample));

            List<OrganizationInvitation> result = service.findAll();

            assertThat(result).hasSize(1).contains(sample);
            verify(dao).findAll();
        }

        @Test
        @DisplayName("returns empty list when no records exist")
        void returnsEmptyList() {
            when(dao.findAll()).thenReturn(Collections.emptyList());

            List<OrganizationInvitation> result = service.findAll();

            assertThat(result).isEmpty();
            verify(dao).findAll();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("returns entity when found")
        void returnsEntity_whenFound() {
            when(dao.findById(1L)).thenReturn(Optional.of(sample));

            Optional<OrganizationInvitation> result = service.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            verify(dao).findById(1L);
        }

        @Test
        @DisplayName("returns null when not found")
        void returnsNull_whenNotFound() {
            when(dao.findById(99L)).thenReturn(Optional.empty());

            Optional<OrganizationInvitation> result = service.findById(99L);

            assertThat(result).isNotPresent();
            verify(dao).findById(99L);
        }
    }

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("persists entity and returns saved instance")
        void persistsEntity() {
            OrganizationInvitation toSave = new OrganizationInvitation();
            when(dao.save(any(OrganizationInvitation.class))).thenReturn(sample);

            OrganizationInvitation result = service.save(toSave);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(dao).save(any(OrganizationInvitation.class));
        }

        @Test
        @DisplayName("generates ref when not present")
        void generatesRef_whenMissing() {
            OrganizationInvitation toSave = new OrganizationInvitation();
            when(dao.save(any(OrganizationInvitation.class))).thenAnswer(inv -> inv.getArgument(0));

            OrganizationInvitation result = service.save(toSave);

            assertThat(result.getRef()).isNotBlank();
        }

        @Test
        @DisplayName("preserves existing ref when already set")
        void preservesRef_whenAlreadySet() {
            OrganizationInvitation toSave = new OrganizationInvitation();
            toSave.setRef("existing-ref");
            when(dao.save(any(OrganizationInvitation.class))).thenAnswer(inv -> inv.getArgument(0));

            OrganizationInvitation result = service.save(toSave);

            assertThat(result.getRef()).isEqualTo("existing-ref");
        }

        @Test
        @DisplayName("returns null when entity is null")
        void returnsNull_whenEntityIsNull() {
            OrganizationInvitation result = service.save(null);

            assertThat(result).isNull();
            verifyNoInteractions(dao);
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("delegates to save")
        void delegatesToSave() {
            OrganizationInvitation toCreate = new OrganizationInvitation();
            when(dao.save(any(OrganizationInvitation.class))).thenReturn(sample);

            OrganizationInvitation result = service.create(toCreate);

            assertThat(result).isNotNull();
            verify(dao).save(any(OrganizationInvitation.class));
        }
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @Test
        @DisplayName("deletes entity when found")
        void deletesEntity_whenFound() {
            when(dao.findById(1L)).thenReturn(Optional.of(sample));
            doNothing().when(dao).deleteById(1L);

            service.deleteById(1L);

            verify(dao).findById(1L);
            verify(dao).deleteById(1L);
        }

        @Test
        @DisplayName("does nothing when entity not found")
        void doesNothing_whenNotFound() {
            when(dao.findById(99L)).thenReturn(Optional.empty());

            service.deleteById(99L);

            verify(dao).findById(99L);
            verify(dao, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("does nothing when id is null")
        void doesNothing_whenIdIsNull() {
            service.deleteById(null);

            verifyNoInteractions(dao);
        }
    }

    @Nested
    @DisplayName("findByRef")
    class FindByRef {

        @Test
        @DisplayName("returns entity matching ref")
        void returnsEntity_whenRefMatches() {
            when(dao.findByRef("organizationinvitation-ref-001")).thenReturn(sample);

            OrganizationInvitation result = service.findByRef("organizationinvitation-ref-001");

            assertThat(result).isNotNull();
            assertThat(result.getRef()).isEqualTo("organizationinvitation-ref-001");
            verify(dao).findByRef("organizationinvitation-ref-001");
        }

        @Test
        @DisplayName("returns null for blank ref without hitting the DAO")
        void returnsNull_whenRefIsBlank() {
            OrganizationInvitation result = service.findByRef("  ");

            assertThat(result).isNull();
            verifyNoInteractions(dao);
        }
    }
}
