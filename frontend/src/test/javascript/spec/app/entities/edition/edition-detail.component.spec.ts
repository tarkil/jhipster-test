import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { JhiLanguageService } from 'ng-jhipster';
import { MockLanguageService } from '../../../helpers/mock-language.service';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EditionDetailComponent } from '../../../../../../main/webapp/app/entities/edition/edition-detail.component';
import { EditionService } from '../../../../../../main/webapp/app/entities/edition/edition.service';
import { Edition } from '../../../../../../main/webapp/app/entities/edition/edition.model';

describe('Component Tests', () => {

    describe('Edition Management Detail Component', () => {
        let comp: EditionDetailComponent;
        let fixture: ComponentFixture<EditionDetailComponent>;
        let service: EditionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [EditionDetailComponent],
                providers: [
                    MockBackend,
                    BaseRequestOptions,
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    {
                        provide: Http,
                        useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
                            return new Http(backendInstance, defaultOptions);
                        },
                        deps: [MockBackend, BaseRequestOptions]
                    },
                    {
                        provide: JhiLanguageService,
                        useClass: MockLanguageService
                    },
                    EditionService
                ]
            }).overrideComponent(EditionDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EditionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EditionService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN
            spyOn(service, 'find').and.returnValue(Observable.of(new Edition(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.edition).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
