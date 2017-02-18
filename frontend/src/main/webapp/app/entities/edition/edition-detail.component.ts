import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Edition } from './edition.model';
import { EditionService } from './edition.service';

@Component({
    selector: 'jhi-edition-detail',
    templateUrl: './edition-detail.component.html'
})
export class EditionDetailComponent implements OnInit, OnDestroy {

    edition: Edition;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private editionService: EditionService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['edition']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.editionService.find(id).subscribe(edition => {
            this.edition = edition;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
