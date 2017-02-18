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
import { ReprintDetailComponent } from '../../../../../../main/webapp/app/entities/reprint/reprint-detail.component';
import { ReprintService } from '../../../../../../main/webapp/app/entities/reprint/reprint.service';
import { Reprint } from '../../../../../../main/webapp/app/entities/reprint/reprint.model';

describe('Component Tests', () => {

    describe('Reprint Management Detail Component', () => {
        let comp: ReprintDetailComponent;
        let fixture: ComponentFixture<ReprintDetailComponent>;
        let service: ReprintService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [ReprintDetailComponent],
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
                    ReprintService
                ]
            }).overrideComponent(ReprintDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReprintDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReprintService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN
            spyOn(service, 'find').and.returnValue(Observable.of(new Reprint(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.reprint).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
