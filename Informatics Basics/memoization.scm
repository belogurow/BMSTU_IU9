(use-syntax (ice-9 syncase))

(define memoized-factorial
  (let ((memo (list (list 0 1))))
    (lambda (n)
      (let ((memoized (assq n memo)))
        (if (not memoized)
            (begin
              (let ((new-value (* n (memoized-factorial (- n 1)))))
                (set! memo (cons (list n new-value) memo))
                new-value))
            (cadr memoized))))))

(define-syntax lazy-cons
  (syntax-rules ()
    ((_ a b) (cons a (delay b)))))

(define (naturals start)
  (lazy-cons start (naturals (+ start 1))))

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

(define (lazy-factorial n)
  (list-ref (spis-fact n) n))

(define (spis-fact n)
  (define (spis a n)
    (if (<= a n)
      (cons (memoized-factorial a) (spis (+ a 1) n))))
  (spis 0 n))