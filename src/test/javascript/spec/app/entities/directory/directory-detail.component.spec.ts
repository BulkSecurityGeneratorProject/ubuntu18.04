/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Ubuntu18TestModule } from '../../../test.module';
import { DirectoryDetailComponent } from 'app/entities/directory/directory-detail.component';
import { Directory } from 'app/shared/model/directory.model';

describe('Component Tests', () => {
    describe('Directory Management Detail Component', () => {
        let comp: DirectoryDetailComponent;
        let fixture: ComponentFixture<DirectoryDetailComponent>;
        const route = ({ data: of({ directory: new Directory(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Ubuntu18TestModule],
                declarations: [DirectoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DirectoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DirectoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.directory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
