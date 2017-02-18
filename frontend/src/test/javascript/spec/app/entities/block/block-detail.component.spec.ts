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
import { BlockDetailComponent } from '../../../../../../main/webapp/app/entities/block/block-detail.component';
import { BlockService } from '../../../../../../main/webapp/app/entities/block/block.service';
import { Block } from '../../../../../../main/webapp/app/entities/block/block.model';

describe('Component Tests', () => {

    describe('Block Management Detail Component', () => {
        let comp: BlockDetailComponent;
        let fixture: ComponentFixture<BlockDetailComponent>;
        let service: BlockService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [BlockDetailComponent],
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
                    BlockService
                ]
            }).overrideComponent(BlockDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BlockDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BlockService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN
            spyOn(service, 'find').and.returnValue(Observable.of(new Block(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.block).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
