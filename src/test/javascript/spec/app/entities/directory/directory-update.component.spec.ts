/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Ubuntu18TestModule } from '../../../test.module';
import { DirectoryUpdateComponent } from 'app/entities/directory/directory-update.component';
import { DirectoryService } from 'app/entities/directory/directory.service';
import { Directory } from 'app/shared/model/directory.model';

describe('Component Tests', () => {
    describe('Directory Management Update Component', () => {
        let comp: DirectoryUpdateComponent;
        let fixture: ComponentFixture<DirectoryUpdateComponent>;
        let service: DirectoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Ubuntu18TestModule],
                declarations: [DirectoryUpdateComponent]
            })
                .overrideTemplate(DirectoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DirectoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DirectoryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Directory(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.directory = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Directory();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.directory = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
