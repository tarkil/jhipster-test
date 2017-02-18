import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Block } from './block.model';
import { BlockService } from './block.service';

@Component({
    selector: 'jhi-block-detail',
    templateUrl: './block-detail.component.html'
})
export class BlockDetailComponent implements OnInit, OnDestroy {

    block: Block;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private blockService: BlockService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['block']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.blockService.find(id).subscribe(block => {
            this.block = block;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
