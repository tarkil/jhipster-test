import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Reprint } from './reprint.model';
import { ReprintService } from './reprint.service';

@Component({
    selector: 'jhi-reprint-detail',
    templateUrl: './reprint-detail.component.html'
})
export class ReprintDetailComponent implements OnInit, OnDestroy {

    reprint: Reprint;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private reprintService: ReprintService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['reprint']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.reprintService.find(id).subscribe(reprint => {
            this.reprint = reprint;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
