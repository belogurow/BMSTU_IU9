 (use-syntax (ice-9 syncase))

(define-syntax lazy-cons
  (syntax-rules ()
    ((_ a b) (cons a (delay b)))))

(define (natural start)
  (lazy-cons start (natural (+ start 1))))

(define (lazy-car p)
  (car p))

(define (lazy-cdr p)
  (force (cdr p)))

(define (lazy-head xs k)
  (if (not (zero? k))
      (cons (car xs) (lazy-head (lazy-cdr xs) (- k 1)))
      '()))     

(define (lazy-ref xs k)
  (if (zero? k)
      (lazy-car xs)
      (lazy-ref (lazy-cdr xs) (- k 1))))
;-----------------------------------------------
(define naturals (natural 0))

(define (lazy-map proc . xs)
  (if (null? (cdr xs))
      (lazy-cons (proc (car (map lazy-car xs))) (apply lazy-map proc (map lazy-cdr xs)))
      (lazy-cons (proc (car (map lazy-car xs)) (car (reverse (map lazy-car xs)))) (apply lazy-map proc (map lazy-cdr xs)))))

(define (lazy-filter pred? xs)
  (if (pred? (lazy-car xs))
      (lazy-cons (lazy-car xs) (lazy-filter pred? (lazy-cdr xs)))
      (lazy-filter pred? (lazy-cdr xs))))
(lazy-head (lazy-map * naturals naturals) 10)