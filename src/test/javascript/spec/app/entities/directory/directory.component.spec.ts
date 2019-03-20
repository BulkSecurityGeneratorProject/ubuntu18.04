/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Ubuntu18TestModule } from '../../../test.module';
import { DirectoryComponent } from 'app/entities/directory/directory.component';
import { DirectoryService } from 'app/entities/directory/directory.service';
import { Directory } from 'app/shared/model/directory.model';

describe('Component Tests', () => {
    describe('Directory Management Component', () => {
        let comp: DirectoryComponent;
        let fixture: ComponentFixture<DirectoryComponent>;
        let service: DirectoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Ubuntu18TestModule],
                declarations: [DirectoryComponent],
                providers: []
            })
                .overrideTemplate(DirectoryComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DirectoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DirectoryService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Directory(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.directories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
